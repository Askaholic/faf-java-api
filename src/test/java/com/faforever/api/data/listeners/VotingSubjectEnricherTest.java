package com.faforever.api.data.listeners;

import com.faforever.api.data.domain.Player;
import com.faforever.api.data.domain.Vote;
import com.faforever.api.data.domain.VotingAnswer;
import com.faforever.api.data.domain.VotingChoice;
import com.faforever.api.data.domain.VotingQuestion;
import com.faforever.api.data.domain.VotingSubject;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.support.MessageSourceAccessor;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class VotingSubjectEnricherTest {
  private VotingSubjectEnricher instance;
  @Mock
  private MessageSourceAccessor messageSourceAccessor;

  @Before
  public void setUp() {
    instance = new VotingSubjectEnricher();
    instance.init(messageSourceAccessor);
  }

  @Test
  public void testQuestionEnhancing() {
    VotingQuestion votingQuestion = new VotingQuestion();
    votingQuestion.setAlternativeQuestion(true);
    votingQuestion.setQuestionKey("abc");
    VotingSubject votingSubject = new VotingSubject();
    votingSubject.setEndOfVoteTime(OffsetDateTime.MIN);
    votingSubject.setRevealWinner(true);
    votingQuestion.setVotingSubject(votingSubject);

    Vote vote1 = new Vote();
    Player player1 = new Player();
    vote1.setPlayer(player1);

    Vote vote2 = new Vote();
    Player player2 = new Player();
    vote2.setPlayer(player2);

    Vote vote3 = new Vote();
    Player player3 = new Player();
    vote1.setPlayer(player3);

    Vote vote4 = new Vote();
    Player player4 = new Player();
    vote1.setPlayer(player4);

    Vote vote5 = new Vote();
    Player player5 = new Player();
    vote1.setPlayer(player5);

    VotingChoice votingChoice = new VotingChoice();
    votingChoice.setId(1);
    votingChoice.setVotingQuestion(votingQuestion);

    addAnswerToChoice(votingChoice, votingQuestion, vote1, 0);
    addAnswerToChoice(votingChoice, votingQuestion, vote2, 0);

    VotingChoice votingChoice2 = new VotingChoice();
    votingChoice2.setId(2);
    votingChoice2.setVotingQuestion(votingQuestion);

    addAnswerToChoice(votingChoice2, votingQuestion, vote3, 0);
    addAnswerToChoice(votingChoice2, votingQuestion, vote4, 0);
    addAnswerToChoice(votingChoice2, votingQuestion, vote5, 1);

    VotingChoice votingChoice3 = new VotingChoice();
    votingChoice3.setId(3);
    votingChoice3.setVotingQuestion(votingQuestion);

    addAnswerToChoice(votingChoice2, votingQuestion, vote5, 0);

    instance.calculateWinners(votingQuestion);

    assertThat(votingQuestion.getWinners(), hasItem(votingChoice2));
  }


  @Test
  public void testQuestionEnhancingDraw() {
    VotingQuestion votingQuestion = new VotingQuestion();
    votingQuestion.setAlternativeQuestion(true);
    votingQuestion.setQuestionKey("abc");
    VotingSubject votingSubject = new VotingSubject();
    votingSubject.setEndOfVoteTime(OffsetDateTime.MIN);
    votingSubject.setRevealWinner(true);
    votingQuestion.setVotingSubject(votingSubject);

    Vote vote1 = new Vote();
    Player player1 = new Player();
    vote1.setPlayer(player1);

    Vote vote2 = new Vote();
    Player player2 = new Player();
    vote2.setPlayer(player2);

    Vote vote3 = new Vote();
    Player player3 = new Player();
    vote1.setPlayer(player3);

    Vote vote4 = new Vote();
    Player player4 = new Player();
    vote1.setPlayer(player4);

    Vote vote5 = new Vote();
    Player player5 = new Player();
    vote1.setPlayer(player5);

    Vote vote6 = new Vote();
    Player player6 = new Player();
    vote1.setPlayer(player6);


    VotingChoice votingChoice = new VotingChoice();
    votingChoice.setId(1);
    votingChoice.setVotingQuestion(votingQuestion);

    addAnswerToChoice(votingChoice, votingQuestion, vote1, 0);
    addAnswerToChoice(votingChoice, votingQuestion, vote2, 0);
    addAnswerToChoice(votingChoice, votingQuestion, vote6, 0);


    VotingChoice votingChoice2 = new VotingChoice();
    votingChoice2.setId(2);
    votingChoice2.setVotingQuestion(votingQuestion);

    addAnswerToChoice(votingChoice2, votingQuestion, vote4, 0);
    addAnswerToChoice(votingChoice2, votingQuestion, vote3, 0);
    addAnswerToChoice(votingChoice2, votingQuestion, vote5, 1);

    VotingChoice votingChoice3 = new VotingChoice();
    votingChoice3.setId(3);
    votingChoice3.setVotingQuestion(votingQuestion);

    addAnswerToChoice(votingChoice2, votingQuestion, vote5, 0);

    instance.calculateWinners(votingQuestion);

    assertThat(votingQuestion.getWinners(), Matchers.allOf(hasItem(votingChoice2), hasItem(votingChoice)));
  }

  private void addAnswerToChoice(VotingChoice votingChoice, VotingQuestion votingQuestion, Vote vote, int alternativeOrdinal) {
    VotingAnswer votingAnswer = new VotingAnswer();
    votingAnswer.setAlternativeOrdinal(alternativeOrdinal);
    votingAnswer.setVote(vote);
    votingAnswer.setVotingChoice(votingChoice);

    if (vote.getVotingAnswers() != null) {
      vote.getVotingAnswers().add(votingAnswer);
    } else {
      vote.setVotingAnswers(new HashSet<>(Collections.singleton(votingAnswer)));
    }

    if (votingChoice.getVotingAnswers() != null) {
      votingChoice.getVotingAnswers().add(votingAnswer);
    } else {
      votingChoice.setVotingAnswers(new HashSet<>(Collections.singleton(votingAnswer)));
    }

    if (votingQuestion.getVotingChoices() != null) {
      votingQuestion.getVotingChoices().add(votingChoice);
    } else {
      votingQuestion.setVotingChoices(new HashSet<>(Collections.singleton(votingChoice)));
    }
  }

}