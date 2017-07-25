package org.mbari.m3.vars.annotation.commands;

import org.mbari.m3.vars.annotation.UIToolBox;
import org.mbari.m3.vars.annotation.events.AnnotationsChangedEvent;
import org.mbari.m3.vars.annotation.model.Annotation;

import java.util.Arrays;

/**
 * @author Brian Schlining
 * @since 2017-05-10T10:05:00
 */
public class UpdateAnnotationCmd implements Command {

    private final Annotation oldAnnotation;
    private final Annotation newAnnotation;

    public UpdateAnnotationCmd(Annotation oldAnnotation, Annotation newAnnotation) {
        this.oldAnnotation = oldAnnotation;
        this.newAnnotation = newAnnotation;
    }

    @Override
    public void apply(UIToolBox toolBox) {
        doAction(toolBox, newAnnotation);
    }

    @Override
    public void unapply(UIToolBox toolBox) {
        doAction(toolBox, oldAnnotation);
    }

    private void doAction(UIToolBox toolBox, Annotation annotation) {
        toolBox.getServices()
                .getAnnotationService()
                .updateAnnotation(annotation)
                .thenAccept(a -> {
                    toolBox.getEventBus()
                            .send(new AnnotationsChangedEvent(null, Arrays.asList(a)));
                });
    }

    @Override
    public String getDescription() {
        return "Update annotation";
    }

    @Override
    public String toString() {
        return "UpdateAnnotationCmd{" +
                "oldAnnotation=" + oldAnnotation +
                ", newAnnotation=" + newAnnotation +
                '}';
    }
}