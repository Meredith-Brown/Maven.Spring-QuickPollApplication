package io.zipcoder.tc_spring_poll_application.controller;

import dtos.OptionCount;
import dtos.VoteResult;
import io.zipcoder.tc_spring_poll_application.domain.Vote;
import io.zipcoder.tc_spring_poll_application.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class ComputeResultController {
    private VoteRepository voteRepository;

    @Autowired
    public ComputeResultController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @RequestMapping(value = "/computeresult", method = RequestMethod.GET)
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        Collection<OptionCount> results = new ArrayList<>(); // each object has id and count
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findVotesByPoll(pollId);
        for (Vote v : allVotes) {
            Long optionId = v.getOption().getId();
            int index = getIndexOfOptionCountById(optionId, (ArrayList<OptionCount>) results);
            if (index >= 0) {
                int count = ((ArrayList<OptionCount>) results).get(index).getCount();
                ((ArrayList<OptionCount>) results).get(index).setCount(1 + (count + 1));
            } else {
                ((ArrayList<OptionCount>) results).add(new OptionCount(optionId, 1));
            }
        }
        voteResult.setResults(results);
        return new ResponseEntity<VoteResult>(voteResult, HttpStatus.OK);
    }

    public int getIndexOfOptionCountById(Long optionId, ArrayList<OptionCount> collectionToSearch) {
        for (int i = 0; i < collectionToSearch.size(); i++) {
            if (collectionToSearch.get(i).getOptionId().equals(optionId)) {
                return i;
            }
        }
        return -1;
    }

}

