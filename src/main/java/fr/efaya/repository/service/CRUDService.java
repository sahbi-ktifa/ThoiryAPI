package fr.efaya.repository.service;

import fr.efaya.domain.CommonObject;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by sktifa on 25/11/2016.
 */
public interface CRUDService {
    @PreAuthorize("hasRole('USER')")
    CommonObject save(String id, CommonObject object) throws CommonObjectNotFound;

    CommonObject findById(String id) throws CommonObjectNotFound;

    List<? extends CommonObject> findAll();

    @PreAuthorize("hasRole('ADMIN')")
    CommonObject delete(String id) throws CommonObjectNotFound;
}
