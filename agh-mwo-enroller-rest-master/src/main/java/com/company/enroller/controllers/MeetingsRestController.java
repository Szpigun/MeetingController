package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;

@RestController
@RequestMapping("/meetings")
public class MeetingsRestController {

	@Autowired
	// ParticipantService participantService;
	MeetingService meetingService;

	@RequestMapping(value = "", method = RequestMethod.GET) // zwraca wszystkie spotkania
	public ResponseEntity<?> getMeeting() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET) // zwrca spotkanie szukajac po nazwie spotkanie
	public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
		Meeting newMeeting = meetingService.findById(meeting.getId());
		if (newMeeting != null) {
			return new ResponseEntity(
					"Unable to create. Meeting with such title " + meeting.getTitle() + " already exist.",
					HttpStatus.CONFLICT);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipantToMeeting(@RequestBody Participant participant,
			@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		meeting.addParticipant(participant);
		meetingService.update(meeting);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meetingService.delete(meeting);
		return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Meeting IncommingFromFrontEndMeeting) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meeting.setTitle(IncommingFromFrontEndMeeting.getTitle());
		meeting.setDescription(IncommingFromFrontEndMeeting.getDescription());
		meeting.setDate(IncommingFromFrontEndMeeting.getDate());
		meetingService.update(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	/*@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipantsOfMeeting(@RequestBody Participant participant, @PathVariable("id") Long id ) {
		Meeting meeting = meetingService.findById(id);
		meeting.getParticipants();
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}",method = RequestMethod.POST)
	public ResponseEntity<?> deleteParticipantFromMeeting(@RequestBody Participant participant, @PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		meeting.removeParticipant(participant);
		meetingService.update(meeting);
		return new ResponseEntity(HttpStatus.OK);
	} */
}
