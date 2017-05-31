package org.mbari.m3.vars.annotation.ui.concepttree;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.mbari.m3.vars.annotation.services.CachedConceptService;
import org.mbari.m3.vars.annotation.services.varskbserver.v1.KBConceptService;
import org.mbari.m3.vars.annotation.services.varskbserver.v1.KBWebServiceFactory;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * @author Brian Schlining
 * @since 2017-05-16T12:48:00
 */
public class SearchableTreePaneDemo extends Application {

    private static CachedConceptService conceptService = new CachedConceptService(
            new KBConceptService(new KBWebServiceFactory("http://m3.shore.mbari.org/kb/v1/")));

    private static ResourceBundle uiBundle = ResourceBundle.getBundle("UIBundle",
            Locale.getDefault());

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
        SearchTreePaneFactory paneBuilder = new SearchTreePaneFactory(conceptService, uiBundle);
        BorderPane node = paneBuilder.build();
        Scene scene = new Scene(node, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }
}