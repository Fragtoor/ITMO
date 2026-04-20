\set person_id random(1, 1000000)
\set meeting_id random(1, 2000000)

BEGIN;
INSERT INTO PersonMeeting (person, meeting) VALUES (:person_id, :meeting_id);
COMMIT;