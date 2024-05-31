package com.lotus.flatmate.conversation.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lotus.flatmate.message.dto.AttachmentDto;
import com.lotus.flatmate.message.model.request.AttachmentRequest;
import com.lotus.flatmate.message.service.AttachmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {
	
	private final AttachmentService attachmentService;
	
	@PostMapping
	public AttachmentDto uploadFile(@RequestBody AttachmentRequest request) {
		return attachmentService.saveFile(request);
	}
	
	@GetMapping("/{id}")
	public AttachmentDto getFile(@PathVariable Long id) {
		return attachmentService.getFile(id);
	}
	
	@GetMapping
	public List<AttachmentDto> getFiles() {
		return attachmentService.getAllFiles();
	}
}
