package site.sg.snserver_spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SnServerSpringApplication

fun main(args: Array<String>) {
    runApplication<SnServerSpringApplication>(*args)
}
