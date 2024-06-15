package site.sg.snserver_spring.video

import org.springframework.stereotype.Service
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import org.springframework.core.io.ByteArrayResource
import org.springframework.util.FileCopyUtils

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
@Service
class YouTubeDataService {
    private val CLIENT_SECRETS = "client_secret.json"
    private val SCOPES = listOf("https://www.googleapis.com/auth/youtube.readonly")
    private val APPLICATION_NAME = "API code samples"
    private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()

    @Throws(IOException::class)
    fun authorize(httpTransport: NetHttpTransport): Credential {
        // Load client secrets.
        val resource = ClassLoader.getSystemResource(CLIENT_SECRETS) // Use ClassLoader directly
        val `in` = resource.openStream()
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(`in`))
        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
            .build()
        return AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")
    }

    @Throws(GeneralSecurityException::class, IOException::class)
    fun getService(): YouTube {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val credential = authorize(httpTransport)
        return YouTube.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build()
    }

    @Throws(GeneralSecurityException::class, IOException::class, GoogleJsonResponseException::class)
    fun getItemsList() {
        val youtubeService = getService()
        // Define and execute the API request
        val request = youtubeService.playlistItems()
            .list(mutableListOf("snippet"))
        val response = request.setMaxResults(50L)
            .setPlaylistId("PLx8rAXc66SGL5ii4likGaGXrFb51-Qqdl")
            .execute()
        println(response)
    }

}
