package sg.snserver.cmd.command

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication(scanBasePackages = ["sg.snserver.hex"], exclude = [DataSourceAutoConfiguration::class])
//@EnableJpaRepositories(basePackages = ["sg.snserver.hex.adapter_outbound.jpa.interfaces"])
@EnableMongoRepositories(basePackages = ["sg.snserver.hex.adapter_outbound.jpa.interfaces"])
@EntityScan(basePackages = ["sg.snserver.hex.adapter_out.jpa.entities"])
@ComponentScan(basePackages = ["sg.snserver.hex"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}