package com.lotus.flatmate.message.service;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lotus.flatmate.message.dto.AttachmentDto;
import com.lotus.flatmate.message.entity.Attachment;
import com.lotus.flatmate.message.mapper.AttachmentMapper;
import com.lotus.flatmate.message.model.request.AttachmentRequest;
import com.lotus.flatmate.message.repository.AttachmentRepository;

import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
	
	private final AttachmentRepository attachmentRepository;
	
	private final AttachmentMapper attachmentMapper;

	@Override
	public AttachmentDto saveFile(AttachmentRequest request) {
		String fileName = UUID.randomUUID().toString().replace("-", "");
		String fileType = (String) Array.get(request.getFileType().split("/"), 1);
		System.out.println(fileType);
		Thread fileOutputThread = new Thread(() -> {
			File file = new File("D://file/"+fileName+"." + fileType);
			try ( FileOutputStream fos = new FileOutputStream(file); ) {
			     
//			      byte[] decoder = Base64.getDecoder().decode(request.getFileBytes());
			      byte[] decoder = Decoders.BASE64.decode(request.getFileBytes());
			      fos.write(decoder);
			      System.out.println(fileType + " saved.");
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
		});
		fileOutputThread.start();
		Attachment attachment = Attachment.builder()
				.fileName(fileName)
				.fileBytes(request.getFileBytes())
				.fileType(request.getFileType())
				.build();
		
		return attachmentMapper.mapToDto(attachmentRepository.save(attachment));
	}

	@Override
	public AttachmentDto getFile(Long id) {
		Attachment attachment = attachmentRepository.findById(id).get();
		return attachmentMapper.mapToDto(attachment);
	}

	@Override
	public List<AttachmentDto> getAllFiles() {
		List<Attachment> attachments = attachmentRepository.findAll();
		return attachments.stream().map(attachmentMapper::mapToDto).toList();
	}

}
