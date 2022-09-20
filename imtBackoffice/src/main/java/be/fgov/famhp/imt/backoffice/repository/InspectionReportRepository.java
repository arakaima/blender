package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.InspectionReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the InspectionReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InspectionReportRepository extends MongoRepository<InspectionReport, String> {}
