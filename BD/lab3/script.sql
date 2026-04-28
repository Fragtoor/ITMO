SELECT DISTINCT meeting, person FROM PersonMeeting pm JOIN Person per ON per.personID = pm.person
WHERE sex = 'M' AND GROUP BY pm.person HAVING COUNT(meeting) > 2 ORDER BY name