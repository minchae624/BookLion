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
		window.location.href = "/mypage.html";
	});

	// 댓글 등록 기능 추가
	const commentBtn = document.querySelector(".btn-blue");
	const commentTextarea = document.getElementById("new-comment");
	const userIdInput = document.getElementById("user-id");

	if (commentBtn) {
		commentBtn.addEventListener("click", async (e) => {
			e.preventDefault();

			const content = commentTextarea.value.trim();
			if (!content) {
				alert("댓글을 입력해주세요.");
				return;
			}

			const postId = window.location.pathname.split("/").pop();
			const token = localStorage.getItem("token");
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
					window.location.reload();
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
