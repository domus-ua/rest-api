FROM openjdk:11
COPY ./target/rest-api-0.0.1-SNAPSHOT.jar app.jar
ADD wait_for_it.sh /
RUN chmod u+x ./wait_for_it.sh
CMD ["./wait_for_it.sh","db:3306","--","java","-jar","app.jar"]

