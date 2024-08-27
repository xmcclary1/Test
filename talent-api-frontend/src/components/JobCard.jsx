import React from 'react';

const JobCard = ({ job, onViewDetails, onEdit, onDelete, onApply, role }) => {
  const { title, company, location, jobType, postedDate } = job;

  const handleCardClick = () => {
    onViewDetails(job, onEdit, onDelete, onApply);
  };

  return (
    <div className="job-card" onClick={handleCardClick}>
      <h3>{title}</h3>
      <p><strong>Company:</strong> {company}</p>
      <p><strong>Location:</strong> {location}</p>
      <p><strong>Type:</strong> {jobType}</p>
      <p><strong>Posted:</strong> {new Date(postedDate).toLocaleDateString()}</p>
    </div>
  );
};

export default JobCard;
