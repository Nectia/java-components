# java-components

Componentes reutilizables java

# 1 Contenido

- [Nectia Helpers](#2-nectia-helpers)
- [Nectia Mail](#3-nectia-mail)
- [Nectia Quartz](#4-nectia-quartz)

# 2 Nectia Helpers

Clases con métodos estaticos solo pueden ser dependiendes del framework spring dado que se utiliza con base 
en casi todos los proyectos

## 2.1 PropertiesAccessor

Permite acceder a propiedades en tiempo de ejecución, es util cuando no se pueden inyectar usando @value

### getProperty(String name)

Ej:

```java
@javax.inject.Named
class Test{
    @javax.inject.Inject
    private PropertiesAccesor propertiesAccesor;
    
    public String getMyProperty(){
        return propertiesAccesor.getProperty("${myproperty}");
    }
    
}
```

## 2.2 RutHelper

Conjunto de métodos estaticos asociados a RUT.

### getRutWithoutFormat(String rut)

Retorna un rut sin formato (elimina '.' y '-'')

Ej:

```java
String rut = "1-9";
String rutWihoutFormat = RutHelper.getRutWithoutFormat(rut);
assertEquals("19", rutWihoutFormat);
```

### isValid(String rut)

Permite verificar si un rut es válido dado su digito verificador (último caracter del rut)

Ej:
```java
boolean rutIsValid = RutHelper.isValid("1-9");
assertTrue(rutIsValid);
```
 
# 3 Nectia Mail

Componentes para poder enviar correos utilizando servicios SMTP

## 3.1 EmailServiceImpl

Impementación de AbstractEmailService, se puede utilizar para enviar correos utilizando un smtp

Ej:

```java
@Configuration
class Config {
    @Bean
    public AbstractEmailService emailService(){
        Properties properties = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        return new EmailServiceImpl(properties, new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication() {
                String username = "user";
                String password = "password";
                return new PasswordAuthentication(username, password);
            }
        });
    }
}
@javax.inject.Named
class Test {
    @javax.inject.Inject
    private AbstractEmailService emailService;
    
    public void sendMail(){
        emailService.sendEmail(new EmailDTO(
                                "from@mail.com", 
                                Collections.singletonList("to@mail.com"), 
                                "subject", 
                                "<html><body><h1>Hello</h1></body></html>"));
    }
}
```

# 4 Nectia Quartz

Componentes para facilizar el Uso de Quartz con Spring

## 4.1 QuartzCron

Anotacion que permite crear un Job para ser ejecutado por Quartz de acuerdon a una expresión.

Ej:

```java
@Configuration
class Config {
    @Bean
    public QuartzJobFactory quartzJobFactory(SchedulerFactoryBean quartzScheduler, PropertiesAccessor propertiesAccessor){
        return new QuartzJobFactory(quartzScheduler, propertiesAccessor, "group");
    }
}

@QuartzCron(cron = "${cron-expresion-property}")
class MyJob implements org.quartz.Job {
    @Override
    public void execute(JobExecutionContext context) {
        //ejecutando job
    }
}
```