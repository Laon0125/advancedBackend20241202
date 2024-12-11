let socket;
let userId, recipientId;

function connect() {
    userId = document.getElementById('userId').value;
    const chattingStatus = document.getElementById('chattingStatus').value;
    const serverPort = $('#serverPort').val();
    socket = new WebSocket('ws://localhost:' + serverPort + '/chat?userId=' + userId + '&status=' + chattingStatus);

    socket.onopen = () => {
        console.log('Connected as ' + userId);
        $('#statusIcon').css({color: 'green', fontWeight: 'bold'});
    }
    socket.onmessage = async(event) => {
        console.log('Message from server: ' + event.data);
        setTimeout(async () => {
            await fetchChatterList();
        }, 200);
    }
    socket.onclose = () => {
        console.log('Disconnected from the server');
        $('#statusIcon').css({color: 'red', fontWeight: 'bold'});
    }
}

async function fetchChatItems() {
    const userId = $('#userId').val();
    const recipientId  = $('#recipientId').val();
    try {
        const response = await fetch(`/chatting/getChatItems?userId=${userId}&recipientId=${recipientId}`);
        if (response.ok) {
            const chatItemsByDate = await response.json();
            updateChatContainer(chatItemsByDate);
        }
    } catch (error) {
        console.error("Failed to fetch messages:", error);
    }
}

function updateChatContainer(messagesByDate) {
    const chatContainer = document.getElementById("chatContainer");
    chatContainer.innerHTML = ""; // 기존 메시지 초기화

    for (const [date, messages] of Object.entries(messagesByDate)) {
        // 날짜 표시
        const dateDiv = document.createElement("div");
        dateDiv.className = "text-center mt-2 mb-3";
        dateDiv.style.fontSize = "0.7rem";
        dateDiv.style.backgroundColor = "lightgrey";
        dateDiv.textContent = date;
        chatContainer.appendChild(dateDiv);

        // 메시지 표시
        messages.forEach(chat => {
            const messageDiv = document.createElement("div");

            if (chat.isMine === 0) {
                // 받은 메시지
                messageDiv.innerHTML = `
                    <div>
                        <img src="${chat.friendImageUrl}" alt="${chat.friendName}" width="28" style="border-radius: 30%">
                        <span style="font-size: 0.6rem;">${chat.friendName}</span>
                    </div>
                    <div class="message received">
                        <p>${chat.message}</p>
                        <span style="font-size: 0.6rem;">${chat.timeStr}</span>
                    </div>
                `;
            } else {
                // 보낸 메시지
                messageDiv.innerHTML = `
                    <div class="message sent">
                        <span style="font-size: 0.6rem; margin-right: 3px;">
                            ${chat.hasRead === 0 ? `<span class="read-status">1</span>` : ''}
                            ${chat.timeStr}
                        </span>
                        <p>${chat.message}</p>
                    </div>
                `;
            }
            chatContainer.appendChild(messageDiv);
        });
    }
    // input tag
    const inputTag = document.createElement("input");
    inputTag.className = "form-control mt-2";
    inputTag.type = "text";
    inputTag.id = "messageInput";
    inputTag.placeholder = "메시지 입력";
    inputTag.addEventListener("keydown", handleEnterKey);
    chatContainer.appendChild(inputTag);

    // 스크롤을 가장 아래로 내리기
    chatContainer.scrollTop = chatContainer.scrollHeight;
}

function handleEnterKey(event) {
    if (event.key === 'Enter') {
        event.preventDefault();     // 줄바꿈 방지(기본 엔터 키 동작 방지)
        sendMessage();
    }
}