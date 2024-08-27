import React, { useState } from 'react';
import ApplicantItem from './ApplicantItem';
import { getApplicationById } from '../services/applicationService'; // Assuming the service file is located here

const ApplicantList = ({ applicationIds, status, title, onApprove, onReject }) => {
  const [applicants, setApplicants] = useState(
    applicationIds.map(id => getApplicationById(id)).filter(applicant => applicant.status === status)
  );

  const handleStatusChange = (updatedApplicant) => {
    setApplicants(prevApplicants => prevApplicants.filter(applicant => applicant.id !== updatedApplicant.id));
  };

  return (
    <div className="applicant-list">
      <h3>{title}</h3>
      {applicants.length > 0 ? (
        applicants.map(applicant => (
          <ApplicantItem 
            key={applicant.id} 
            applicant={applicant} 
            onApprove={(id) => { onApprove(id); handleStatusChange(applicant); }} 
            onReject={(id) => { onReject(id); handleStatusChange(applicant); }} 
          />
        ))
      ) : (
        <p>No applicants in this category.</p>
      )}
    </div>
  );
};

export default ApplicantList;
//const getApplicationById = (id) is needed for backend service file