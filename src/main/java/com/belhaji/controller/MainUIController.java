package com.belhaji.controller;

import com.belhaji.entity.Action;
import com.belhaji.entity.LogEntry;
import com.belhaji.entity.Session;
import com.belhaji.entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class MainUIController implements Initializable
{

  public static final String FILENAME = "/ui/main.fxml";
  private static final Long SESSION_DURATION = 30L * 60 * 1000;
  private Set<User> users;
  private List<LogEntry> logEntries;

  @FXML
  private DatePicker startDate;
  @FXML
  private DatePicker endDate;
  @FXML
  private BarChart<String, Number> dureeChart;

  @FXML
  private BarChart<String, Number> nbChart;

  @FXML
  private AreaChart<String, Number> nbMoyenneChart;

  @FXML
  private MenuItem openFileMenuItem;

  @FXML
  private MenuItem closeMenuItem;

  @FXML
  private AreaChart<String, Number> dureeMoyenneChart;

  @FXML
  private VBox vbox;

  @FXML
  private Button applyBtn;
  private File file;

  @Override
  public void initialize(URL location, ResourceBundle resources)
  {
    this.applyBtn.setOnAction(this::onApply);
    this.closeMenuItem.setOnAction(e -> Platform.exit());
    this.openFileMenuItem.setOnAction(this::onOpenFile);
    this.nbChart.setTitle("Action count by user Id");
    this.nbMoyenneChart.setTitle("Average Action count by Session by user Id");
    this.dureeChart.setTitle("Usage duration by days by user id");
    this.dureeMoyenneChart.setTitle("Usage average duration by days by user id");
  }

  private void onOpenFile(ActionEvent event)
  {
    this.file = new FileChooser().showOpenDialog(null);
    this.logEntries = this.loadData(this.file.getAbsolutePath());
    this.logEntries.sort(Comparator.comparingLong(e -> e.getTimestamp().getTime()));
    Date startDate = this.logEntries.get(0).getTimestamp();
    Date endDate = this.logEntries.get(this.logEntries.size() - 1).getTimestamp();
    this.startDate.setValue(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    this.endDate.setValue(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    this.onApply(null);
  }

  private void onApply(ActionEvent event)
  {
    if (this.file == null)
    {
      Alert alert = new Alert(Alert.AlertType.ERROR, "You need to choose a file first", ButtonType.OK);
      alert.show();
      return;
    }
    Date startDate;
    Date endDate;
    this.nbChart.getData().clear();
    if (this.validateDates())
    {
      startDate = Date.from(Instant.from(this.startDate.getValue().atStartOfDay(ZoneId.systemDefault())));
      endDate = Date.from(Instant.from(this.endDate.getValue().atStartOfDay(ZoneId.systemDefault())));
    }
    else
    {
      return;
    }
    List<LogEntry> mLogEntries = this.logEntries.stream()
        .filter(logEntry -> logEntry.getTimestamp().getTime() >= startDate.getTime() && logEntry.getTimestamp().getTime() <= endDate.getTime())
        .collect(Collectors.toList());
    this.users = this.getUsers(mLogEntries, SESSION_DURATION);
    this.drawNbChart();
    this.drawNbMoyenneChart();
    this.drawDureeChart();
    this.drawDureeMoyenneChart();
  }

  private void drawNbChart()
  {
    this.nbChart.getData().clear();
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("User Id");
    this.users
        .forEach(user -> {
          long count = user.getSessions()
              .stream()
              .flatMap(session -> session.getActions().stream())
              .map(Action::getActionDate)
              .count();
          series.getData().add(new XYChart.Data<>(String.valueOf(user.getId()), count));
        });

    this.nbChart.getData().add(series);
  }

  private void drawNbMoyenneChart()
  {
    this.nbMoyenneChart.getData().clear();
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("User Id");
    this.users
        .forEach(user -> {
          long count = user.getSessions()
              .stream()
              .flatMap(session -> session.getActions().stream())
              .map(Action::getActionDate)
              .count();
          double avg = (double) count / user.getSessions().size();
          series.getData().add(new XYChart.Data<>(String.valueOf(user.getId()), avg));
        });

    this.nbMoyenneChart.getData().add(series);
  }

  private void drawDureeChart()
  {
    this.dureeChart.getData().clear();
    this.dureeChart.setManaged(true);
    XYChart.Series<String, Number> series = new XYChart.Series<>();

    series.setName("User Id");
    this.users
        .forEach(user -> {
          Date min = user.getSessions()
              .stream()
              .flatMap(session -> session.getActions().stream())
              .map(Action::getActionDate)
              .min(Comparator.comparingLong(Date::getTime))
              .get();
          Date max = user.getSessions()
              .stream()
              .flatMap(session -> session.getActions().stream())
              .map(Action::getActionDate)
              .max(Comparator.comparingLong(Date::getTime))
              .get();
          Duration duration = Duration.between(min.toInstant(), max.toInstant());
          series.getData().add(new XYChart.Data<>(String.valueOf(user.getId()), duration.toDays()));
        });

    this.dureeChart.getData().add(series);
  }

  private void drawDureeMoyenneChart()
  {
    this.dureeMoyenneChart.getData().clear();
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("User Id");
    this.users
        .forEach(user -> {
          Date min = user.getSessions()
              .stream()
              .flatMap(session -> session.getActions().stream())
              .map(Action::getActionDate)
              .min(Comparator.comparingLong(Date::getTime))
              .get();
          Date max = user.getSessions()
              .stream()
              .flatMap(session -> session.getActions().stream())
              .map(Action::getActionDate)
              .max(Comparator.comparingLong(Date::getTime))
              .get();
          Duration duration = Duration.between(min.toInstant(), max.toInstant());
          double avg = (double) duration.toDays() / user.getSessions().size();
          series.getData().add(new XYChart.Data<>(user.getId().toString(), avg));
        });

    this.dureeMoyenneChart.getData().add(series);
  }

  private boolean validateDates()
  {
    if (this.startDate.getValue() == null || this.endDate.getValue() == null)
    {
      Alert alert = new Alert(Alert.AlertType.ERROR, "You have to select a period", ButtonType.OK);
      alert.show();
      return false;
    }
    return true;
  }

  private Set<User> getUsers(List<LogEntry> logEntries, Long sessionDurationInMilis)
  {
    return this.groupByUserId(logEntries)
        .entrySet()
        .stream()
        .map((entry) -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparingLong(v -> v.getTimestamp().getTime())).collect(Collectors.toList())))
        .map((entry) -> {
          User user = new User();
          user.setId(entry.getKey());
          this.groupByPeriod(entry.getValue(), sessionDurationInMilis, entry.getValue().get(0).getTimestamp().getTime())
              .entrySet().stream().sorted(Comparator.comparingInt(a -> Math.toIntExact(a.getKey())))
              .map((kk) -> {
                Session session = new Session();
                session.setId(kk.getKey());
                if (!kk.getValue().isEmpty())
                {
                  session.setStartDate(kk.getValue().get(0).getTimestamp());
                  session.setEndDate(kk.getValue().get(kk.getValue().size() - 1).getTimestamp());
                }
                kk.getValue().forEach(value -> session.getActions().add(new Action(value.getTimestamp())));
                return session;
              }).forEach(session -> user.getSessions().add(session));
          return user;
        })
        .collect(Collectors.toSet());
  }

  private List<LogEntry> loadData(String filename)
  {
    File file = new File(filename);
    try (
        CSVParser parse = CSVParser.parse(Paths.get(file.getAbsolutePath()),
                                          Charset.defaultCharset(),
                                          CSVFormat.newFormat(';')
                                              .withCommentMarker('#')
                                              .withFirstRecordAsHeader())
    )
    {

      DateFormat dateFormat = new SimpleDateFormat("dd MMM yy, HH:mm");
      return parse.getRecords().stream().map(r -> {
        LogEntry logEntry = new LogEntry();
        try
        {
          logEntry.setId(Integer.parseInt(r.get(0)));
          logEntry.setTimestamp(dateFormat.parse((r.get(1))));
        }
        catch (ParseException e)
        {

          e.printStackTrace();
        }
        return logEntry;
      })
          .collect(Collectors.toList());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }


  private Map<Long, List<LogEntry>> groupByPeriod(List<LogEntry> logEntries, Long periodInSeconds, Long start)
  {
    return logEntries.stream()
        .collect(Collectors.groupingBy(logEntry -> (logEntry.getTimestamp().getTime() - start) / periodInSeconds));
  }

  private Map<Integer, List<LogEntry>> groupByUserId(List<LogEntry> logEntries)
  {
    return logEntries.stream()
        .collect(Collectors.groupingBy(LogEntry::getId));
  }
}
