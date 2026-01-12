package com.modubank.account.domain.exception

class RequiredFieldMissingException(field: String) :
    DomainException("${field}_must_not_be_blank")
