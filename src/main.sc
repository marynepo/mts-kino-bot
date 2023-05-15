require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /

    state: Start
        q!: $regex</start>
        a: Начнём.

    state: Hello
        intent!: /привет
        a: Привет привет

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
