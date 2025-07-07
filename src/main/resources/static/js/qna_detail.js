document.addEventListener("DOMContentLoaded", () => {
	const msgDiv = document.getElementById("message");
	if (msgDiv) {
		const message = msgDiv.dataset.msg;
		if (message) alert(message);
	}
	loadAnswers();
});

const questionId = document.getElementById("question-id").value;
const answerListDiv = document.getElementById("answer-list");
const answerTextarea = document.getElementById("new-answer");
const answerSubmitBtn = document.getElementById("btn-submit-answer");
const userId = document.getElementById("user-id")?.value;
const questionAuthorId = document.getElementById("question-author-id")?.value;

async function loadAnswers() {
	const res = await fetch(`/api/questions/${questionId}/answers?timestamp=${new Date().getTime()}`);
	if (!res.ok) {
		answerListDiv.innerHTML = "<p>답변을 불러오는 데 실패했습니다.</p>";
		return;
	}

	const answers = await res.json();
	answerListDiv.innerHTML = "";

	const isAlreadyAccepted = answers.some(a => a.isAccepted === "Y");

	const questionStatus = document.getElementById("question-status").value;
	const statusText = document.getElementById("question-status-text");
	if (questionStatus === "solved") {
		statusText.textContent = "해결";
		statusText.style.color = "green";
	} else {
		statusText.textContent = "미해결";
		statusText.style.color = "red";
	}

	answers.forEach(answer => {
		const div = document.createElement("div");
		div.classList.add("answer-item");
		div.style = "background-color:#eee; padding:1rem; border-radius:6px; margin-bottom:1rem;";

		let userButtons = "";
		let badge = "";

		if (String(answer.answerStatus).toUpperCase() === "Y") {
			badge = `<span class="badge-accepted" style="color:blue; font-weight:bold;">[채택]</span>`;
		} else if (!isAlreadyAccepted && String(userId) === String(questionAuthorId)) {
			userButtons += `<button class="btn-accept" data-id="${answer.answerId}">채택</button>`;
		}


		if (String(userId) === String(answer.userId)) {
			userButtons += `
				<button class="btn-edit" data-id="${answer.answerId}">수정</button>
				<button class="btn-delete" data-id="${answer.answerId}">삭제</button>
			`;
		}

		div.innerHTML = `
			<div style="display:flex; justify-content:space-between;">
				<p><strong>${answer.username}</strong> ${badge}</p>
				<div>${userButtons}</div>
			</div>
			<p class="content">${answer.content}</p>
			<p style="text-align:right; font-size:0.9rem;">${new Date(answer.writingtime).toLocaleString()}</p>
		`;
		answerListDiv.appendChild(div);
	});

	setupEditDeleteEvents();
}


window.deleteQuestion = function(questionId) {
	if (confirm("정말 삭제하시겠습니까?")) {
		fetch(`/questions/delete/${questionId}`, {
			method: 'POST'
		})
			.then(res => {
				if (res.redirected) {
					alert("삭제되었습니다.");
					window.location.href = res.url;
				} else {
					alert("삭제 실패!");
				}
			})
			.catch(err => {
				console.error("삭제 오류:", err);
				alert("삭제 중 오류가 발생했습니다.");
			});
	}
}


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
			const div = btn.closest(".answer-item");
			const contentP = div.querySelector(".content");

			if (!contentP || div.querySelector("textarea")) return;

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
				const res = await fetch(`/api/answers/${btn.dataset.id}`, {
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

	document.querySelectorAll(".btn-accept").forEach(btn => {
		btn.addEventListener("click", async () => {
			const answerId = btn.dataset.id;

			if (!confirm("이 답변을 채택하시겠습니까?")) return;

			const res = await fetch("/api/answers/accept", {
				method: "POST",
				headers: {
					"Content-Type": "application/x-www-form-urlencoded"
				},
				body: `answerId=${answerId}`
			});

			if (res.ok) {
				btn.style.display = "none";
				const badge = document.createElement("span");
				badge.classList.add("badge-accepted");
				badge.style.cssText = "color:blue; font-weight:bold; margin-right:5px;";
				badge.textContent = "[채택]";
				btn.parentElement.prepend(badge);

				const statusText = document.getElementById("question-status-text");
				document.getElementById("question-status-text").textContent = "해결";
				document.getElementById("question-status-text").style.color = "green";

				document.getElementById("question-status").value = "solved";

				await loadAnswers();

				alert("답변이 채택되었습니다.");
			} else {
				alert("채택 실패");
			}
		});
	});

}
