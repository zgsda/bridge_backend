package bridge.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Entity
@Table(name = "t_chattingroom")

public class ChattingEntity {
	@Id	//엔티티의 기본키(PK)
	@GeneratedValue(strategy= GenerationType.AUTO)	// 기본키 생성 전략 (DB에서 제공하는 키 생성 전략을 따른다)
	private int roomIdx;
	@Column(nullable = false)
	private String userId1;
	@Column(nullable = false)
	private String userId2;
}