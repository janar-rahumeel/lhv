package ee.lhv.homework.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import jakarta.validation.spi.ValidationProvider;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class BaseConfiguration {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public ValidationProvider<?> validationProvider() {
        return new HibernateValidator();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename("classpath:messages");
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        return reloadableResourceBundleMessageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToEnable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToEnable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
    }

}
