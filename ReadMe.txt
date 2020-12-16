Reproduce the error
=====================

To reproduce the issue with run:

!! Passing test !!
mvn test -Dtest=nz.bignell.bede.camel.ReproduceUnableToLoadProperties

My issue was a missing spring bean of type 'PropertySourcesPlaceholderConfigurer' that would then be able to use the property sources defined with the @TestPropertySource annotation. See commit b04db2f367b035f962af92cf4cc400a13bda27e3
