package com.loopin.streaming_service.infrastructure.srs

import com.fasterxml.jackson.annotation.JsonProperty

data class SrsWebhookDto(
    @JsonProperty("action")
    val action: String?,
    
    @JsonProperty("client_id")
    val clientId: String?,
    
    @JsonProperty("ip")
    val ip: String?,
    
    @JsonProperty("vhost")
    val vhost: String?,
    
    @JsonProperty("app")
    val app: String?,
    
    @JsonProperty("stream")
    val stream: String?,
    
    @JsonProperty("param")
    val param: String?,
    
    @JsonProperty("server_id")
    val serverId: String?,
    
    @JsonProperty("service_id")
    val serviceId: String?,
    
    @JsonProperty("tcUrl")
    val tcUrl: String?,
    
    @JsonProperty("stream_url")
    val streamUrl: String?,
    
    @JsonProperty("stream_id")
    val streamId: String?
)