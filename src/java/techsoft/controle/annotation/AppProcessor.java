package techsoft.controle.annotation;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes("techsoft.controle.annotation.App")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class AppProcessor extends AbstractProcessor{
 
 
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv){
 
        for (TypeElement element : annotations) {
            System.out.println(element.getQualifiedName());
        }
        return true;
    }
}
