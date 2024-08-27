package com.example.talent_api.service;

import com.example.talent_api.domain.JobListing;
import com.example.talent_api.exception.UnauthorizedException;
import com.example.talent_api.repository.JobListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobListingService {

    @Autowired
    private JobListingRepository jobListingRepository;

    public List<JobListing> getAllJobListings() {
        return jobListingRepository.findAll();
    }

    public Optional<JobListing> getJobListingById(String id) {
        return jobListingRepository.findById(id);
    }

    public List<JobListing> getJobListingsByManager(String managerId) {
        return jobListingRepository.findByPostedBy(managerId);
    }

    public JobListing createJobListing(JobListing jobListing) {
        return jobListingRepository.save(jobListing);
    }

//    public void deleteJobListing(String id) {
//        jobListingRepository.deleteById(id);
//    }

    public void deleteJobListing(String id, String managerId) {
        Optional<JobListing> jobListing = jobListingRepository.findById(id);
        if (jobListing.isPresent() && jobListing.get().getPostedBy().equals(managerId)) {
            jobListingRepository.deleteById(id);
        } else {
            throw new UnauthorizedException("You do not have permission to delete this job listing.");
        }
    }

    public void softDeleteJobListing(String id, String managerId) {
        Optional<JobListing> jobListing = jobListingRepository.findById(id);
        if (jobListing.isPresent() && jobListing.get().getPostedBy().equals(managerId)) {
            JobListing listing = jobListing.get();
            listing.setDeleted(true);
            jobListingRepository.save(listing);
        } else {
            throw new UnauthorizedException("You do not have permission to delete this job listing.");
        }
    }

    public List<JobListing> getAllActiveJobListings() {
        return jobListingRepository.findAll().stream()
                .filter(jobListing -> !jobListing.isDeleted())
                .collect(Collectors.toList());
    }

    public JobListing updateJobListing(String id, JobListing jobListing) {
        if (jobListingRepository.existsById(id)) {
            jobListing.setId(id);
            return jobListingRepository.save(jobListing);
        } else {
            return null;
        }
    }

}
