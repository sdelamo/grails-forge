package org.grails.forge.cli.command

import io.micronaut.configuration.picocli.PicocliRunner
import org.grails.forge.cli.CodeGenConfig
import org.grails.forge.options.Language
import org.grails.forge.utils.CommandSpec
import spock.lang.Ignore

class CreateControllerSpec extends CommandSpec {

    @Ignore
    void "test create-controller command"() {
        when:
        generateProject(Language.DEFAULT_OPTION)
        applicationContext.createBean(CodeGenConfig.class, new CodeGenConfig())

        then:
        applicationContext.getBean(CodeGenConfig.class)

        when:
        PicocliRunner.run(CreateControllerCommand.class, applicationContext, "greetings")

        then:
        new File(dir, "grails-app/controllers/example/grails/GreetingsController.groovy").exists()

    }

    @Override
    String getTempDirectoryPrefix() {
        return "test-app"
    }
}
