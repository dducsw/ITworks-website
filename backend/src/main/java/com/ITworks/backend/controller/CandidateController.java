// package com.ITworks.backend.controller;

// import com.ITworks.backend.dto.ResponseModel;
// import com.ITworks.backend.entity.Apply;
// import com.ITworks.backend.entity.Candidate;
// import com.ITworks.backend.service.CandidateService;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/candidates")
// public class CandidateController {
    
//     private final CandidateService candidateService;
    
//     public CandidateController(CandidateService candidateService) {
//         this.candidateService = candidateService;
//     }
    
//     @GetMapping
//     public ResponseEntity<?> getAllCandidates() {
//         try {
//             List<Candidate> candidates = candidateService.findAllCandidates();
//             return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
//                     200,
//                     candidates,
//                     "Retrieved all candidates successfully"
//             ));
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
//                     500,
//                     null,
//                     e.getMessage()
//             ));
//         }
//     }
    
//     @GetMapping("/{id}")
//     public ResponseEntity<?> getCandidateById(@PathVariable Integer id) {
//         try {
//             return candidateService.findCandidateById(id)
//                     .map(candidate -> ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
//                             200,
//                             candidate,
//                             "Candidate retrieved successfully"
//                     )))
//                     .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
//                             400,
//                             null,
//                             "Candidate not found with id: " + id
//                     )));
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
//                     500,
//                     null,
//                     e.getMessage()
//             ));
//         }
//     }
    
//     // Application related endpoints
    
//     @PostMapping("/{candidateId}/applications")
//     public ResponseEntity<?> applyForJob(
//             @PathVariable Integer candidateId,
//             @RequestParam Integer jobId,
//             @RequestParam(required = false) String coverLetter,
//             @RequestParam(required = false) String uploadCv) {
//         try {
//             Apply application = candidateService.applyForJob(candidateId, jobId, coverLetter, uploadCv);
//             return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel(
//                     201,
//                     application,
//                     "Application submitted successfully"
//             ));
//         } catch (IllegalArgumentException | IllegalStateException e) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
//                     400,
//                     null,
//                     e.getMessage()
//             ));
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
//                     500,
//                     null,
//                     e.getMessage()
//             ));
//         }
//     }
    
//     @DeleteMapping("/{candidateId}/applications/{jobId}")
//     public ResponseEntity<?> cancelApplication(
//             @PathVariable Integer candidateId,
//             @PathVariable Integer jobId) {
//         try {
//             candidateService.cancelApplication(candidateId, jobId);
//             return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
//                     200,
//                     null,
//                     "Application canceled successfully"
//             ));
//         } catch (IllegalArgumentException | IllegalStateException e) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
//                     400,
//                     null,
//                     e.getMessage()
//             ));
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
//                     500,
//                     null,
//                     e.getMessage()
//             ));
//         }
//     }
    
//     @GetMapping("/{candidateId}/applications")
//     public ResponseEntity<?> getCandidateApplications(@PathVariable Integer candidateId) {
//         try {
//             List<Apply> applications = candidateService.getCandidateApplications(candidateId);
//             return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
//                     200,
//                     applications,
//                     "Applications retrieved successfully"
//             ));
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
//                     400,
//                     null,
//                     e.getMessage()
//             ));
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
//                     500,
//                     null,
//                     e.getMessage()
//             ));
//         }
//     }
// }
