//package sg.snserver.hex.adapter_outbound.jpa.entities
//
//import jakarta.persistence.Entity
//import jakarta.persistence.Id
//import jakarta.persistence.OneToMany
//import jakarta.persistence.Table
//import java.util.*
//
//@Entity
//@Table(name = "sn_play_item")
//data class PlayListQueuesEntity(
//    @Id
//    val id: UUID,
//
//    @OneToMany(mappedBy = "sn_queue_list")
//    var prev: MutableList<QueueListEntity>,
//
//    @OneToMany(mappedBy = "sn_queue_list")
//    var next: MutableList<QueueListEntity>,
//)
