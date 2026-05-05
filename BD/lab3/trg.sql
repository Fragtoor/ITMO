CREATE OR REPLACE FUNCTION check_last_participant()
RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT COUNT(*)
        FROM PersonMeeting
        WHERE meeting = OLD.meeting
          AND person != OLD.person) < 2
    THEN
        RAISE EXCEPTION 'Нельзя удалить последних двух участников встречи %', OLD.meeting;
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg
BEFORE DELETE ON PersonMeeting
FOR EACH ROW
EXECUTE FUNCTION check_last_participant();

-- запрещает добавлять человека на встречу, если у него в это же самое время уже назначена другая встреча
CREATE OR REPLACE FUNCTION trg_check_time()
RETURNS TRIGGER AS $$
DECLARE
    v_new_meeting_time TIMESTAMP;
    v_conflict_meeting_id INT;
BEGIN
    -- Узнаем время встречи, на которую пытаются записать человека
    SELECT dateTimeMeeting INTO v_new_meeting_time
    FROM Meeting
    WHERE meetingID = NEW.meeting;

    -- Ищем, нет ли у этого человека другой встречи в это же время
    SELECT pm.meeting INTO v_conflict_meeting_id
    FROM PersonMeeting pm
    JOIN Meeting m ON pm.meeting = m.meetingID
    WHERE pm.person = NEW.person
      AND m.dateTimeMeeting = v_new_meeting_time
    LIMIT 1;

    IF v_conflict_meeting_id IS NOT NULL THEN
        RAISE NOTICE 'Конфликт расписания. Персонаж % уже занят на встрече % в это время (%)', 
                        NEW.person, v_conflict_meeting_id, v_new_meeting_time;
        RETURN NULL;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_2
BEFORE INSERT ON PersonMeeting
FOR EACH ROW
EXECUTE FUNCTION trg_check_time();
