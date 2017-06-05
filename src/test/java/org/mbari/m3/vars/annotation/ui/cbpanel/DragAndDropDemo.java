package org.mbari.m3.vars.annotation.ui.cbpanel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.mbari.m3.vars.annotation.EventBus;
import org.mbari.m3.vars.annotation.services.CachedConceptService;
import org.mbari.m3.vars.annotation.services.varskbserver.v1.KBConceptService;
import org.mbari.m3.vars.annotation.services.varskbserver.v1.KBWebServiceFactory;
import org.mbari.m3.vars.annotation.ui.DemoConstants;
import org.mbari.m3.vars.annotation.ui.concepttree.SearchTreePaneController;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * @author Brian Schlining
 * @since 2017-06-01T17:01:00
 */
public class DragAndDropDemo extends Application {

    private static CachedConceptService conceptService = DemoConstants.newConceptService();
    private static EventBus eventBus = DemoConstants.EVENT_BUS;
    private static ResourceBundle uiBundle = DemoConstants.UI_BUNDLE;

    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<Void> f = conceptService.prefetch();
        while (!f.isDone()) {
            Thread.sleep(20);
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(true);
        SearchTreePaneController paneBuilder = new SearchTreePaneController(conceptService, uiBundle);
        BorderPane node = paneBuilder.getRoot();
        FlowPane pane = new FlowPane();
        pane.setPrefSize(800, 250);
        DragPaneDecorator dragPaneDecorator = new DragPaneDecorator(conceptService, eventBus, uiBundle);
        dragPaneDecorator.decorate(pane);
        node.setBottom(pane);
        Scene scene = new Scene(node, 800, 800);
        scene.getStylesheets().add("/application.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
