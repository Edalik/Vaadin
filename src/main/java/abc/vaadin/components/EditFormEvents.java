package abc.vaadin.components;

import com.vaadin.flow.component.ComponentEvent;

public class EditFormEvents {
    public abstract static class EditFormEvent extends ComponentEvent<EditForm<?>> {
        protected EditFormEvent(EditForm source) {
            super(source, false);
        }
    }

    public static class SaveEvent extends EditFormEvent {
        public SaveEvent(EditForm source) {
            super(source);
        }
    }

    public static class DeleteEvent extends EditFormEvent {
        public DeleteEvent(EditForm source) {
            super(source);
        }
    }
}
