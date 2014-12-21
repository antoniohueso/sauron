package com.corpme.sauron.domain;

import com.corpme.sauron.domain.IssueVersion;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
public interface VersionRepository extends Repository<IssueVersion, Long> {

}
