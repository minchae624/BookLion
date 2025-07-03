document.addEventListener("DOMContentLoaded", () => {
	const msgDiv = document.getElementById("message");
	if (msgDiv) {
		const message = msgDiv.dataset.msg;
		if (message) {
			alert(message);
		}
	}
});

const questionId = document.getElementById("question-id").value;
const answerListDiv = document.getElementById("answer-list");
const answerTextarea = document.getElementById("new-answer");
const answerSubmitBtn = document.getElementById("btn-submit-answer");
const userId = document.getElementById("user-id")?.value;

//답변 목록
/*
async function loadAnswers() {
	const res = await fetch(`/api/questions/${questionId}/answers`);
	if (!res.ok) {
		answerListDiv.innerHTML = "<p>답변을 불러오는 데 실패했습니다.</p>";
		return;
	}

	const answers = await res.json();
	answerListDiv.innerHTML = "";

	answers.forEach(answer => {
		const div = document.createElement("div");
		div.style = "background-color:#eee; padding:1rem; border-radius:6px; margin-bottom:1rem;";

		let buttons = "";
		let badge = "";

		

		// 본인 작성 답변에만 수정/삭제 버튼
		if (String(userId) === String(answer.userId)) {
			buttons = `
		              <button class="btn-edit" data-id="${answer.answerId}">수정</button>
		              <button class="btn-delete" data-id="${answer.answerId}">삭제</button>
		            `;
		}
		// 채택 표시
				if (answer.isAccepted === 'Y') {
					badge = `<span style="color:blue; font-weight:bold;">[채택됨]</span>`;
				}
		
		div.innerHTML = `
            <p style="text-align:right;">${buttons}</p>
            <p><strong>${answer.username}</strong></p>
            <p class="content">${answer.content}</p>
            <p style="text-align:right; font-size:0.9rem;">${new Date(answer.writingtime).toLocaleString()}</p>
          `;
		answerListDiv.appendChild(div);
	});

	setupEditDeleteEvents();
}*/

answerSubmitBtn.addEventListener("click", async () => {
	const content = answerTextarea.value.trim();
	if (!content) return alert("답변을 입력해주세요.");

	const res = await fetch(`/api/questions/${questionId}/answers`, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ content, userId: Number(userId) })
	});

	if (res.ok) {
		answerTextarea.value = "";
		loadAnswers();
	} else {
		const err = await res.text();
		alert("등록 실패: " + err);
	}
});

function setupEditDeleteEvents() {
	document.querySelectorAll(".btn-edit").forEach(btn => {
		btn.addEventListener("click", async () => {
			const id = btn.dataset.id;
			const div = btn.closest("div");
			const contentP = div.querySelector(".content");

			if (div.querySelector("textarea")) return;

			const textarea = document.createElement("textarea");
			textarea.value = contentP.textContent;
			textarea.style.width = "100%";

			const saveBtn = document.createElement("button");
			saveBtn.textContent = "저장";

			const cancelBtn = document.createElement("button");
			cancelBtn.textContent = "취소";
			cancelBtn.style.marginLeft = "0.5rem";

			contentP.style.display = "none";
			div.append(textarea, saveBtn, cancelBtn);

			saveBtn.addEventListener("click", async () => {
				const newContent = textarea.value.trim();
				const res = await fetch(`/api/answers/${id}`, {
					method: "PUT",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify({ content: newContent })
				});
				if (res.ok) {
					alert("수정 완료");
					loadAnswers();
				} else {
					alert("수정 실패");
				}
			});

			cancelBtn.addEventListener("click", () => {
				textarea.remove();
				saveBtn.remove();
				cancelBtn.remove();
				contentP.style.display = "block";
			});
		});
	});

	document.querySelectorAll(".btn-delete").forEach(btn => {
		btn.addEventListener("click", async () => {
			const id = btn.dataset.id;
			if (!confirm("정말 삭제하시겠습니까?")) return;
			const res = await fetch(`/api/answers/${id}`, { method: "DELETE" });
			if (res.ok) {
				alert("삭제 완료");
				loadAnswers();
			} else {
				alert("삭제 실패");
			}
		});
	});
}

document.addEventListener("DOMContentLoaded", loadAnswers);
