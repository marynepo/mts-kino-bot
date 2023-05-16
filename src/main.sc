require: timmetable.csv
  name = timetable
  var = timetable
  
require: slotfilling/slotFilling.sc
  module = sys.zb-common

theme: /

    state: Start
        q!: $regex</start>
        script:
            $context.session = {};
            $context.client = {};
            $context.temp = {};
            $context.response = {};
        a: Здравствуйте! Чем я могу вам помочь?

    state: Hello
        intent!: /Расписание
        if: 

    state: Bye
        intent!: /пока
        a: Пока пока

    state: KnowledgeBase
        intentGroup!: /KnowledgeBase
        a: Нашёл ответ в базе знаний!
        script: $faq.pushReplies();

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}