import csv
import random
from datetime import datetime, timedelta

COUNT_PERSON = 1_000_000
COUNT_RESIDENCE = 500_000
COUNT_MEETING = 2_000_000
COUNT_PERSON_MEETING = 5_000_000
COUNT_PERSON_STATE = 3_000_000

def gen_csv():
    print("Генерация PlaceOfResidence...")
    with open('residence.csv', 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        for i in range(1, COUNT_RESIDENCE + 1):
            writer.writerow([
                round(random.uniform(-90, 90), 4), round(random.uniform(-180, 180), 4),
                "Country_"+str(random.randint(1, 50)), "City_"+str(i), "Street_"+str(i), random.randint(1, 200)
            ])

    print("Генерация Person...")
    with open('person.csv', 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        for i in range(1, COUNT_PERSON + 1):
            writer.writerow([
                f"Name_{i}", f"Surname_{i}", random.randint(1, 2), 
                random.choice(['M', 'F']), round(random.uniform(150, 200), 1),
                f"Job_{random.randint(1, 100)}", random.randint(1, COUNT_RESIDENCE)
            ])

    print("Генерация Meeting...")
    start_date = datetime(2450, 1, 1)
    with open('meeting.csv', 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        for i in range(1, COUNT_MEETING + 1):
            m_date = start_date + timedelta(seconds=random.randint(0, 31536000))
            writer.writerow([
                m_date.strftime('%Y-%m-%d %H:%M:%S'), 
                round(random.uniform(-90, 90), 4), round(random.uniform(-180, 180), 4),
                f"Description of meeting {i}", random.randint(0, 100)
            ])

    print("Генерация PersonMeeting (связи)...")
    with open('person_meeting.csv', 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        for i in range(1, COUNT_PERSON_MEETING + 1):
            writer.writerow([random.randint(1, COUNT_PERSON), random.randint(1, COUNT_MEETING)])

    print("Генерация PersonState...")
    with open('person_state.csv', 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        for i in range(1, COUNT_PERSON_STATE + 1):
            s_date = start_date + timedelta(seconds=random.randint(0, 31536000))
            writer.writerow([
                s_date.strftime('%Y-%m-%d %H:%M:%S'), 
                random.randint(0, 100), random.randint(1, COUNT_PERSON), random.randint(1, 12)
            ])

if __name__ == "__main__":
    gen_csv()
