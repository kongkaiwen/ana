package kb;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class QuestionBuffer {

	Queue<Question> questions;
	
	public QuestionBuffer() {
		questions = new LinkedList<Question>();
	}
	
	public void add( Question question ) {
		questions.add(question);
	}
	
	public Queue<Question> getQuestions() {
		return this.questions;
	}
	
	public Question pop() {
		return questions.poll();
	}
}
