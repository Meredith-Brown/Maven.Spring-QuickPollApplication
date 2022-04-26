package io.zipcoder.tc_spring_poll_application.controller;

import io.zipcoder.tc_spring_poll_application.domain.Vote;
import io.zipcoder.tc_spring_poll_application.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class VoteController {
    private VoteRepository voteRepository;

    @Autowired
    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @RequestMapping(value="/votes", method=RequestMethod.GET)
    public ResponseEntity<Iterable<Vote>> getAllVotes() {
        Iterable<Vote> allVotes = voteRepository.findAll();
        return new ResponseEntity<>(allVotes, HttpStatus.OK);
    }

    @RequestMapping(value = "/polls/{pollId}/votes", method = RequestMethod.POST)
    public ResponseEntity<?> createVote(@PathVariable Long pollId, @RequestBody Vote vote) {
        vote = voteRepository.save(vote);
        URI newVoteUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(vote.getId())
                .toUri();
        HttpHeaders header = new HttpHeaders();
        header.setLocation(newVoteUri);
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.CREATED);
    }

//    @RequestMapping(value = "/polls/{pollId}/votes", method = RequestMethod.POST)
//    public ResponseEntity<?> createVote(@PathVariable Long pollId, @RequestBody Vote vote) {
//        vote = voteRepository.save(vote);
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
//                buildAndExpand(vote.getId()).toUri());
//        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED); // is null correct?
//    }

}
