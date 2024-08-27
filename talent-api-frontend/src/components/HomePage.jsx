import React, { useState, useEffect } from 'react';
import axios from 'axios';
import JobList from './JobList';
import ProfilePage from './ProfilePage';
import JobDetail from './JobDetail';
import JobForm from './JobForm';
import { getUserRole } from '../services/userService';
import { getAllJobListings } from '../services/JobListingService';

const HomePage = () => {
  const [role, setRole] = useState('');
  const [jobListings, setJobListings] = useState([]);
  const [userJobListings, setUserJobListings] = useState([]);
  const [selectedJob, setSelectedJob] = useState(null);
  const [isCreatingJob, setIsCreatingJob] = useState(false);
  const [showJobDetail, setShowJobDetail] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await getUserRole();
        const userRole = response.data.role;
        setRole(userRole);

        const userResponse = await axios.get('/api/user/joblistings');
        setUserJobListings(userResponse.data);

        if (userRole === 'candidate') {
          const allJobListings = await getAllJobListings();
          setJobListings(allJobListings.data);
        }
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  }, []);

  const handleViewDetails = (job) => {
    setSelectedJob(job);
    setShowJobDetail(true);
    setIsCreatingJob(false);
  };

  const handleCreateJob = () => {
    setSelectedJob(null);
    setIsCreatingJob(true);
    setShowJobDetail(false);
  };

  const handleEditComplete = (updatedJob) => {
    setUserJobListings(prevListings => prevListings.map(j => j.id === updatedJob.id ? updatedJob : j));
    setShowJobDetail(false);
  };

  const handleDeleteComplete = (jobId) => {
    setUserJobListings(prevListings => prevListings.filter(j => j.id !== jobId));
    setShowJobDetail(false);
  };

  const handleSubmitJobForm = async (job) => {
    try {
      if (job.id) {
        handleEditComplete(job);
      } else {
        const response = await axios.post('/job-listings', job);
        setUserJobListings([...userJobListings, response.data]);
      }
      setIsCreatingJob(false);
    } catch (error) {
      console.error("Error submitting job form:", error);
    }
  };

  return (
    <div className="home-page">
      <header>
        <h1>Welcome to Job Portal</h1>
        <button onClick={() => <ProfilePage />}>Profile</button>
      </header>

      {role === 'manager' ? (
        <>
          <h2>Your Job Listings</h2>
          <JobList jobListings={userJobListings} onViewDetails={handleViewDetails} />
          <button onClick={handleCreateJob}>Create New Job</button>
        </>
      ) : (
        <>
          <h2>Your Applications</h2>
          <JobList jobListings={userJobListings} isHorizontal={true} />  {/* Horizontal list for applied jobs */}
          <h2>Available Job Listings</h2>
          <JobList jobListings={jobListings} onViewDetails={handleViewDetails} />  {/* Vertical list for job search */}
        </>
      )}

      {showJobDetail && selectedJob && (
        <JobDetail
          job={selectedJob}
          role={role}
          onEditComplete={handleEditComplete}
          onDeleteComplete={handleDeleteComplete}
        />
      )}

      {isCreatingJob && (
        <JobForm onSubmit={handleSubmitJobForm} />
      )}
    </div>
  );
};

export default HomePage;
