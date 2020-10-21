package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Controller
//@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private NoteService noteService;
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;
    private FileMapper fileMapper;

    public HomeController(UserService userService, NoteService noteService, CredentialMapper credentialMapper, EncryptionService encryptionService, FileMapper fileMapper) {
        this.userService = userService;
        this.noteService = noteService;
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
        this.fileMapper = fileMapper;
    }

    @GetMapping("/home")
    public String getHomePage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        model.addAttribute("notes", this.noteService.getAllNotes(user.getUserId()));

        List<Credential> credentialList = this.credentialMapper.getAllCredentials(user.getUserId());

        model.addAttribute("credentials", credentialList);

        List<File> fileList = this.fileMapper.getAllFiles(user.getUserId());

        model.addAttribute("files", fileList);

        return "home";
    }

    @GetMapping("/home/viewFile")
    ResponseEntity<Resource> getFile(Authentication authentication, @RequestParam("id") int fileId) {
        User user = userService.getUser(authentication.getName());
        File file = fileMapper.getFile(fileId);
        if (file.getUserId() == user.getUserId()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContentType())).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                    + file.getFileName() + "\"").body(new
                            ByteArrayResource(file.getFileDataBytes()));
        }
        return null;
    }

    @GetMapping("/home/deleteFile")
    public String deleteFile(Authentication authentication, Model model, @RequestParam("id") int fileId) {
        User user = userService.getUser(authentication.getName());
        File file = fileMapper.getFile(fileId);
        if (file.getUserId() == user.getUserId()) {
            fileMapper.delete(fileId);
        }

        return "redirect:";
    }

    @GetMapping("/home/deleteNote")
    public String deleteNote(Authentication authentication, Model model, @RequestParam("id") int noteId) {
        User user = userService.getUser(authentication.getName());
        Note note = noteService.getNote(noteId);
        if (note.getUserId() == user.getUserId()) {
            noteService.delete(noteId);
        }

        return "redirect:";
    }

    @GetMapping("/home/deleteCredential")
    public String deleteCredential(Authentication authentication, Model model, @RequestParam("id") int credentialId) {
        User user = userService.getUser(authentication.getName());
        Credential credential = credentialMapper.getCredential(credentialId);
        if (credential.getUserId() == user.getUserId()) {
            credentialMapper.delete(credentialId);
        }

        return "redirect:";
    }

    @GetMapping("/home/decryptCredPassword/{credentialId}")
    @ResponseBody
    public Credential decryptCredPassword(Authentication authentication, @PathVariable(name = "credentialId") int credentialId) {
        User user = userService.getUser(authentication.getName());
        //Integer credentialId = Integer.parseInt(credentialID);
        Credential credential = credentialMapper.getCredential(credentialId);
        if (credential.getUserId() == user.getUserId()) {
            String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
            credential.setPassword(decryptedPassword);
        }

        return credential;
    }

    @PostMapping("/home/addFile")
    public String addFile(@RequestParam("fileUpload") MultipartFile fileUpload, Model model, Authentication authentication) throws IOException {
        String username = authentication.getName();
        User user = userService.getUser(username);

        System.out.println(fileUpload.getContentType() + " " + fileUpload.getOriginalFilename() + " " + fileUpload.getSize() + " " + user.getUserId() + " " + fileUpload.getBytes()                                       );

        fileMapper.insert(new File(1, fileUpload.getOriginalFilename(), fileUpload.getContentType(), fileUpload.getSize(), user.getUserId(), fileUpload.getBytes()));

        return "redirect:";
    }

    @PostMapping("/home/addNote")
    public String addNote(@ModelAttribute Note note, Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        note.setUserId(user.getUserId());

        if(note.getNoteId() != null) {
            noteService.updateNote(note);
        } else {
            noteService.createNote(note);
        }


        return "redirect:";
    }

    @PostMapping("/home/addCredential")
    public String addCredential(@ModelAttribute Credential credential, Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        credential.setUserId(user.getUserId());

        if(credential.getCredentialId() != null) {
            credentialMapper.updateCredential(credential);
        } else {
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            String encodedKey = Base64.getEncoder().encodeToString(key);

            credential.setKey(encodedKey);
            String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
            credential.setPassword(encryptedPassword);
            credentialMapper.insert(credential);
        }

        return "redirect:";
    }
}
