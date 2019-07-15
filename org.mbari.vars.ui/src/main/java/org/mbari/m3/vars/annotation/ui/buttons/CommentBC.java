package org.mbari.m3.vars.annotation.ui.buttons;


import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;
import org.mbari.m3.vars.annotation.UIToolBox;
import org.mbari.m3.vars.annotation.commands.CreateAssociationsCmd;
import org.mbari.m3.vars.annotation.ui.Icons;
import org.mbari.vars.services.model.Annotation;
import org.mbari.vars.services.model.Association;
import org.mbari.vars.javafx.util.JFXUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Brian Schlining
 * @since 2017-09-11T16:59:00
 */
public class CommentBC extends AbstractBC {

    private TextInputDialog dialog;
    private final String commentLinkName;

    public CommentBC(Button button, UIToolBox toolBox) {
        super(button, toolBox);
        this.commentLinkName = toolBox.getConfig().getString("app.annotation.sample.association.comment");
    }

    protected void init() {
        String tooltip = toolBox.getI18nBundle().getString("buttons.comment");
//        Text icon = iconFactory.createIcon(MaterialIcon.INSERT_COMMENT, "30px");
        FontIcon icon = new FontIcon(Material.INSERT_COMMENT);
        icon.setIconSize(30);
        initializeButton(tooltip, icon);

    }

    private TextInputDialog getDialog() {
        if (dialog == null) {
            ResourceBundle i18n = toolBox.getI18nBundle();
//            Text icon = iconFactory.createIcon(MaterialIcon.INSERT_COMMENT, "30px");
            Text icon = Icons.INSERT_COMMENT.size(30);
            dialog = new TextInputDialog();
            dialog.setTitle(i18n.getString("buttons.comment.dialog.title"));
            dialog.setHeaderText(i18n.getString("buttons.comment.dialog.header"));
            dialog.setContentText(i18n.getString("buttons.comment.dialog.content"));
            dialog.setGraphic(icon);
            dialog.getDialogPane().getStylesheets().addAll(toolBox.getStylesheets());
        }
        return dialog;
    }

    protected void apply() {
        List<Annotation> annotations = new ArrayList<>(toolBox.getData().getSelectedAnnotations());
        TextInputDialog dialog = getDialog();
        JFXUtilities.runOnFXThread(() -> dialog.getEditor().requestFocus());
        Optional<String> s = dialog.showAndWait();
        s.ifPresent(comment -> {
            Association a = new Association(commentLinkName, Association.VALUE_SELF, comment);
            toolBox.getEventBus()
                    .send(new CreateAssociationsCmd(a, annotations));
        });
        getDialog().getEditor().setText(null);

    }
}
