SELECT DISTINCT pm.meeting, per.name FROM PersonMeeting pm JOIN Person per ON per.personID = pm.person
WHERE sex = 'M' GROUP BY pm.person HAVING COUNT(meeting) > 2