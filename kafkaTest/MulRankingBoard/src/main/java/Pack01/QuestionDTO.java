package Pack01;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDTO {

	User user;
    String idQ;
    int resultAttempt;
    int bscore;

    @Builder
    public QuestionDTO(User user, String idQ, int resultAttempt, int bscore) {
        this.user = user;
    	this.idQ = idQ;
    	this.resultAttempt = resultAttempt;
    	this.bscore = bscore;
    }
}