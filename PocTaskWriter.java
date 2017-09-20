package poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class PocTaskWriter {

    public void upsertTaskEventToReportTask(MongoTemplate mongoTemplateTask, List<TaskEvent> taskEvents, String handlerClassName, String eventPackageName) {

        if(taskEvents!=null && taskEvents.size()>0) {

            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.register(TaskMongoConfig.class);
            ctx.refresh();
            TaskEventHandler handler = new TaskEventHandler(mongoTemplateTask);
            if (handler ==null)
                log.error("Event handler is null");
            else if(mongoTemplateTask==null)
                log.error("mongoTemplateTask is null");
            else {
                taskEvents.stream().forEach(event->
                {
                    try {
                        Class<?> handlerclass = Class.forName(handlerClassName);//EventHandler.class;
                        Class<?> eventclass = Class.forName(eventPackageName +event.getClassName());
                        Method serverMethod = handlerclass.getMethod("on", eventclass);
                        serverMethod.invoke(handler, (eventclass).cast(event));
                    } catch (ClassNotFoundException cnfe) {
                        log.error("Class not found exception, classname={}", event.getClassName());
                    } catch (NoSuchMethodException nsme) {
                        log.error("No such method exception, classname={}", event.getClassName());
                    } catch (IllegalAccessException iae) {
                        //log.error(iae.getStackTrace()[0].toString());
                        iae.printStackTrace();
                    } catch (InvocationTargetException ite) {
                        //log.error(ite.getStackTrace()[0].toString());
                        ite.printStackTrace();
                    }
                });
            }

            ctx.register(TaskMongoConfig.class);
            ctx.close();
        }
    }

}
