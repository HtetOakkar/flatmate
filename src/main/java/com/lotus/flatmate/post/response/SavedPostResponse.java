package com.lotus.flatmate.post.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavedPostResponse {
	private Long id;
	private PostDetailsResponse post;
}
