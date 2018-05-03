import com.doszhan.SOAPServiceStub;
import com.doszhan.SOAPServiceStub.Article;
import com.doszhan.SOAPServiceStub.Author;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.List;
import java.util.Optional;

public class ClientMainFrame extends Application {
    private Controller handler = new Controller();
    private HBox buttonBox;
    private ObservableList<Article> articlesList = FXCollections.observableArrayList();
    private TableView<Article> table = new TableView<>(articlesList);

    private Optional<Article> showAddDialog() {
        Dialog<Article> dialog = new Dialog<>();
        dialog.setTitle("Добавить");
        dialog.setHeaderText("Добавить статью");

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField articleID = new TextField();
        TextField articleTitle = new TextField();
        TextField name = new TextField();
        TextField surname = new TextField();

        DatePicker articleDatePicker1 = new DatePicker();
        DatePicker authorDatePicker = new DatePicker();

        grid.add(new Label("Идентификатор:"), 0, 0);
        grid.add(articleID, 1, 0);
        grid.add(new Label("Название статьи:"), 0, 1);
        grid.add(articleTitle, 1, 1);
        grid.add(new Label("Год издания статьи:"), 0, 2);
        grid.add(articleDatePicker1, 1, 2);
        grid.add(new Label("Имя издателя:"), 0, 3);
        grid.add(name, 1, 3);
        grid.add(new Label("Фамилия издателя:"), 0, 4);
        grid.add(surname, 1, 4);
        grid.add(new Label("Год рождения издателя:"), 0, 5);
        grid.add(authorDatePicker, 1, 5);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        BooleanBinding idInvalid = Bindings.createBooleanBinding(() ->
                        articleID.getText().trim().isEmpty() || !articleID.getText().chars().allMatch(Character::isDigit),
                articleID.textProperty()
        );
        BooleanBinding articleTitleInvalid = Bindings.createBooleanBinding(() ->
                        articleTitle.getText().trim().isEmpty(),
                surname.textProperty()
        );
        BooleanBinding yearInvalid = Bindings.createBooleanBinding(() ->
                        articleDatePicker1.getValue() == null,
                articleDatePicker1.valueProperty().isNull()
        );
        BooleanBinding nameInvalid = Bindings.createBooleanBinding(() ->
                        name.getText().trim().isEmpty(),
                name.textProperty()
        );
        BooleanBinding surnameInvalid = Bindings.createBooleanBinding(() ->
                        surname.getText().trim().isEmpty(),
                surname.textProperty()
        );
        BooleanBinding birthYearInvalid = Bindings.createBooleanBinding(() ->
                        authorDatePicker.getValue() == null,
                authorDatePicker.valueProperty().isNull()
        );

        addButton.disableProperty().bind(
                idInvalid
                        .or(articleTitleInvalid)
                        .or(yearInvalid)
                        .or(nameInvalid)
                        .or(surnameInvalid)
                        .or(birthYearInvalid));

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(articleID::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Article article = new Article();
                article.setId(Integer.valueOf(articleID.getText()));
                article.setTitle(articleTitle.getText());

                SOAPServiceStub.Date date = new SOAPServiceStub.Date();
                date.setYear(articleDatePicker1.getValue().getYear());
                date.setDay(articleDatePicker1.getValue().getDayOfMonth());
                date.setMonth(articleDatePicker1.getValue().getMonth().getValue());
                article.setDate(date);

                SOAPServiceStub.Date authorDate = new SOAPServiceStub.Date();
                authorDate.setYear(authorDatePicker.getValue().getYear());
                authorDate.setDay(authorDatePicker.getValue().getDayOfMonth());
                authorDate.setMonth(authorDatePicker.getValue().getMonth().getValue());

                Author author = new Author();
                author.setName(name.getText());
                author.setSurname(surname.getText());
                author.setBirthDay(authorDate);

                article.setAuthor(author);
                return article;
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void setupButtons() {
        Button addButton = new Button("Добавить статью");
        addButton.setOnAction(e -> {
            Optional<Article> articleInfo = showAddDialog();
            articleInfo.ifPresent(article -> handler.add(article));
            updateTable();
        });

        Button exitButton = new Button("Выход");
        exitButton.setOnAction(e -> {
            Platform.exit();
        });

        buttonBox = new HBox();
        buttonBox.setSpacing(5);
        buttonBox.setPadding(new Insets(5, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(addButton, exitButton);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Сборник статей на тему \"Java\"");
        Scene scene = new Scene(new VBox());
        scene.setFill(Color.DARKBLUE);

        setupButtons();
        setupTable();

        ((VBox) scene.getRoot()).setPadding(new Insets(5, 5, 5, 5));
        ((VBox) scene.getRoot()).getChildren().addAll(table, buttonBox);

        ((VBox) scene.getRoot()).setVgrow(table, Priority.ALWAYS);
        handler.connect("http://localhost:8080/axis2/services/SOAPService?wsdl");
        updateTable();

        //stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void setupTable() {
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Article, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setMaxWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setSortType(TableColumn.SortType.ASCENDING);

        TableColumn TitleColumn = new TableColumn("Название статьи");
        TitleColumn.setMinWidth(275);

        TableColumn<Article, String> titleColumn = new TableColumn<>("Название статьи");
        titleColumn.setMinWidth(100);
        titleColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTitle()));
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setOnEditCommit(t -> {
            Article article = t.getRowValue();
            article.setTitle(String.valueOf(t.getNewValue()));
            handler.edit(article);
        });

        TableColumn<Article, SOAPServiceStub.Date> yearColumn = new TableColumn<>("Год издания статьи");
        yearColumn.setMinWidth(80);
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
        yearColumn.setOnEditCommit(t -> {
            Article article = t.getRowValue();
            article.setDate(t.getNewValue());
            handler.edit(article);
        });


        TableColumn authorColumn = new TableColumn("Издатель");
        authorColumn.setMinWidth(275);

        TableColumn<Article, String> authorFirstNameColumn = new TableColumn<>("Имя");
        authorFirstNameColumn.setMinWidth(100);
        authorFirstNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getAuthor().getName()));
        authorFirstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorFirstNameColumn.setOnEditCommit(t -> {
            Article article = t.getRowValue();
            article.getAuthor().setName(t.getNewValue());
            handler.edit(article);
        });

        TableColumn<Article, String> authorLastNameColumn = new TableColumn<>("Фамилия");
        authorLastNameColumn.setMinWidth(100);
        authorLastNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getAuthor().getSurname()));
        authorLastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorLastNameColumn.setOnEditCommit(t -> {
            Article article = t.getRowValue();
            article.getAuthor().setSurname(t.getNewValue());
            handler.edit(article);
        });

        TableColumn<Article, String> authorBirthdayColumn = new TableColumn<>("Год рождения");
        authorBirthdayColumn.setMinWidth(90);

        authorBirthdayColumn.setCellValueFactory(p -> {
            SimpleStringProperty property = new SimpleStringProperty();
            SOAPServiceStub.Date date = p.getValue().getAuthor().getBirthDay();

            property.setValue(date.getDay() + "." + date.getMonth() + "." + date.getYear());
            return property;
        });

        authorBirthdayColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorBirthdayColumn.setOnEditCommit(t -> {
            Article article = t.getRowValue();
            String[] strings = t.getNewValue().split("\\.");
            if (strings.length < 3) {
                return;
            }

            SOAPServiceStub.Date date = new SOAPServiceStub.Date();
            date.setDay(Integer.valueOf(strings[0]));
            date.setMonth(Integer.valueOf(strings[1]));
            date.setYear(Integer.valueOf(strings[2]));

            article.getAuthor().setBirthDay(date);
            handler.edit(article);
        });

        authorColumn.getColumns().addAll(authorFirstNameColumn, authorLastNameColumn, authorBirthdayColumn);
        table.getColumns().addAll(idColumn, titleColumn, yearColumn, authorColumn);

        ArticlesContextMenu ctxMenu = new ArticlesContextMenu(table);
        table.setContextMenu(ctxMenu);
    }

    private void updateTable() {
        articlesList.clear();
        List<Article> articles = handler.getArticle();
        this.articlesList.addAll(articles);
    }

    private class ArticlesContextMenu extends ContextMenu {
        ArticlesContextMenu(TableView<Article> table) {
            MenuItem removeItem = new MenuItem("Удалить");
            MenuItem refreshItem = new MenuItem("Обновить");

            removeItem.setOnAction(e -> {
                if (table.getSelectionModel().getSelectedItem() == null) {
                    return;
                }
                handler.remove(table.getSelectionModel().getSelectedItem());
                updateTable();

                e.consume();
            });

            refreshItem.setOnAction(e -> {
                updateTable();
                e.consume();
            });

            getItems().addAll(removeItem, refreshItem);

        }
    }

    public class DateStringConverter extends StringConverter<SOAPServiceStub.Date> {
        @Override
        public String toString(SOAPServiceStub.Date object) {
            if (object == null) {
                return "";
            }

            return Integer.toString(object.getDay()) + '.' + Integer.toString(object.getMonth()) + '.' + Integer.toString(object.getYear());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SOAPServiceStub.Date fromString(String value) {
            // If the specified value is null or zero-length, return null
            if (value == null) {
                return null;
            }

            value = value.trim();

            if (value.length() < 1) {
                return null;
            }

            String[] strings = value.split("\\.");
            if (strings.length < 3) {
                return null;
            }

            SOAPServiceStub.Date date = new SOAPServiceStub.Date();
            date.setDay(Integer.valueOf(strings[0]));
            date.setMonth(Integer.valueOf(strings[1]));
            date.setYear(Integer.valueOf(strings[2]));

            return date;
        }
    }

}
