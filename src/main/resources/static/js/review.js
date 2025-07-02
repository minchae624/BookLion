document.addEventListener("DOMContentLoaded", async () => {
	const welcomeMsg = document.getElementById("welcome-msg");
	const logoutBtn = document.getElementById("logout-btn");
	const mypageBtn = document.getElementById("mypage-btn");

	const token = localStorage.getItem("token");

	// 사용자 정보 가져오기
	if (token) {
		try {
			const res = await fetch("/api/users/me", {
				method: "GET",
				headers: {
					"Authorization": "Bearer " + token
				}
			});

			if (res.ok) {
				const user = await res.json();
				welcomeMsg.textContent = `환영합니다. ${user.username}님`;
				const userIdInput = document.getElementById("user-id");
				const userNameInput = document.getElementById("user-name");
				if (userIdInput) userIdInput.value = user.userId;
				if (userNameInput) userNameInput.value = user.username;

				/* 글쓴이와 접속자가 일치한지 확인 */
				const postWriterInput = document.getElementById("post-writer");
				const editDeleteBtn = document.getElementById("edit-delete-btn");

				if (postWriterInput && editDeleteBtn) {
					if (String(user.userId) !== String(postWriterInput.value)) {
						editDeleteBtn.style.display = "none";
					}
				}
			} else {
				welcomeMsg.textContent = "환영합니다.";
			}
		} catch (err) {
			console.error("사용자 정보 조회 실패:", err);
			welcomeMsg.textContent = "환영합니다.";
		}
	} else {
		welcomeMsg.textContent = "환영합니다.";
	}

	// 글 삭제 처리
	window.deletePost = function(postId) {
		if (confirm("정말 삭제하시겠습니까?")) {
			fetch(`/api/posts/delete/${postId}`, {
				method: 'DELETE'
			}).then(res => {
				if (res.ok) {
					alert("삭제되었습니다.");
					window.location.href = "/api/posts";
				} else {
					alert("삭제 실패!");
				}
			});
		}
	}

	// 로그아웃 처리
	logoutBtn.addEventListener("click", () => {
		localStorage.removeItem("token");
		alert("로그아웃되었습니다.");
		window.location.href = "/";
	});

	// 마이페이지 이동
	mypageBtn.addEventListener("click", () => {
		if (!token) {
			alert("로그인이 필요합니다.");
			return;
		}
		window.location.href = "/mypage";
	});

	// 댓글 등록 기능 추가
	const commentBtn = document.querySelector(".btn-blue");
	const commentTextarea = document.getElementById("new-comment");
	const userIdInput = document.getElementById("user-id");
	const replyListDiv = document.getElementById("reply-list");

	const postId = window.location.pathname.split("/").pop();

	// 댓글 목록 불러오기 함수
	async function loadReplies() {
		if (!postId) return;
		try {
			const res = await fetch(`/api/posts/${postId}/replies`, {
				headers: {
					"Authorization": "Bearer " + token
				}
			});
			if (res.ok) {
				const replies = await res.json();
				replyListDiv.innerHTML = ''; // 초기화

				replies.forEach(reply => {
				    const replyDiv = document.createElement('div');
				    replyDiv.style = "background-color: #eee; padding: 1rem; margin-top: 1rem; border-radius: 6px;";
				    replyDiv.innerHTML = `
				        <p style="text-align: right; font-size: 0.9rem;">
				            <!-- 수정/삭제 기능은 추후 구현 -->
				        </p>
				        <p><strong>${reply.username}</strong></p>  <!-- 여기 username으로 변경 -->
				        <p>${reply.content}</p>
				        <p style="text-align: right; font-size: 0.9rem;">${new Date(reply.writingtime).toLocaleString()}</p>
				    `;
				    replyListDiv.appendChild(replyDiv);
				});
			} else {
				replyListDiv.innerHTML = '<p>댓글을 불러오는 데 실패했습니다.</p>';
			}
		} catch (err) {
			console.error("댓글 불러오기 오류:", err);
			replyListDiv.innerHTML = '<p>댓글을 불러오는 중 오류가 발생했습니다.</p>';
		}
	}

	// 페이지 로드 시 댓글 목록 먼저 불러오기
	if (replyListDiv) {
		loadReplies();
	}

	// 댓글 등록
	if (commentBtn && commentTextarea && userIdInput) {
		commentBtn.addEventListener("click", async (e) => {
			e.preventDefault();

			const content = commentTextarea.value.trim();
			if (!content) {
				alert("댓글을 입력해주세요.");
				return;
			}

			const userId = userIdInput.value;

			if (!token) {
				alert("로그인이 필요합니다.");
				return;
			}

			if (!userId) {
				alert("사용자 정보가 없습니다. 다시 로그인 해주세요.");
				return;
			}

			try {
				const res = await fetch(`/api/posts/${postId}/replies`, {
					method: "POST",
					headers: {
						"Content-Type": "application/json",
						"Authorization": "Bearer " + token
					},
					body: JSON.stringify({ content, userId: Number(userId) })
				});

				if (res.ok) {
					alert("댓글이 등록되었습니다.");
					commentTextarea.value = '';  // 입력란 초기화
					loadReplies();  // 댓글 목록 새로고침
				} else {
					const error = await res.text();
					alert("댓글 등록 실패: " + error);
				}
			} catch (err) {
				console.error("댓글 등록 오류:", err);
				alert("댓글 등록 중 오류가 발생했습니다.");
			}
		});
	}
});