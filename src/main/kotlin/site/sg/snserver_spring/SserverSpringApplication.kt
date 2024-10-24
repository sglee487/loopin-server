package site.sg.snserver_spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class SnServerSpringApplication

fun main(args: Array<String>) {
    runApplication<SnServerSpringApplication>(*args)
}
