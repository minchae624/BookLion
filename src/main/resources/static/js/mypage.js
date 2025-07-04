document.addEventListener("DOMContentLoaded", () => {
	const token = localStorage.getItem("token");
	if (!token) {
		alert("로그인이 필요합니다.");
		window.location.href = "/login";
		return;
	}

	const userIdField = document.getElementById("user-id");
	const passwordField = document.getElementById("user-password");
	const emailField = document.getElementById("user-email");
	const logoutBtn = document.getElementById("logout-btn");
	const mypageBtn = document.getElementById("mypage-btn");
	const welcomeMsg = document.getElementById("welcome-msg");

	let username = "";
	let writtenData = [];
	let commentedData = [];
	let likedData = [];

	fetch("/api/users/me", {
		headers: {
			Authorization: `Bearer ${token}`,
		},
	})
		.then((res) => res.json())
		.then((data) => {
			userIdField.value = data.username;
			welcomeMsg.textContent = `환영합니다. ${data.username} 님`;
			passwordField.value = "**********";
			emailField.value = data.email;
			username = data.username;

			fetch("/api/mypage/posts?username=" + username, {
				headers: { Authorization: `Bearer ${token}` }
			})
				.then(res => res.json())
				.then(data => {
					writtenData = data;
					renderPaginatedList(data, "written-list", "written-pagination", "/review");
				});

			fetch("/api/mypage/comments?username=" + username, {
				headers: { Authorization: `Bearer ${token}` }
			})
				.then(res => res.json())
				.then(data => {
					commentedData = data;
				});

			fetch("/api/mypage/likes?username=" + username, {
				headers: { Authorization: `Bearer ${token}` }
			})
				.then(res => res.json())
				.then(data => {
					likedData = data;
				});
		})
		.catch(() => {
			alert("회원정보를 불러오지 못했습니다.");
		});

	document.getElementById("update-password-btn").addEventListener("click", async () => {
		const newPassword = passwordField.value;
		if (!newPassword) return alert("새 비밀번호를 입력하세요.");

		try {
			const res = await fetch("/api/users/me", {
				method: "PUT",
				headers: {
					"Content-Type": "application/json",
					Authorization: `Bearer ${token}`,
				},
				body: JSON.stringify({ password: newPassword }),
			});

			if (!res.ok) throw new Error();
			alert("비밀번호가 수정되었습니다.");
		} catch {
			alert("비밀번호 수정 실패");
		}
	});

	document.getElementById("update-email-btn").addEventListener("click", async () => {
		const newEmail = emailField.value;
		if (!newEmail) return alert("이메일을 입력하세요.");

		try {
			const res = await fetch("/api/users/me", {
				method: "PUT",
				headers: {
					"Content-Type": "application/json",
					Authorization: `Bearer ${token}`,
				},
				body: JSON.stringify({ email: newEmail }),
			});

			if (!res.ok) throw new Error();
			alert("이메일이 수정되었습니다.");
		} catch {
			alert("이메일 수정 실패");
		}
	});

	document.getElementById("delete-account-btn").addEventListener("click", async () => {
		if (!confirm("정말 탈퇴하시겠습니까?")) return;

		try {
			const res = await fetch("/api/users/delete", {
				method: "DELETE",
				headers: {
					Authorization: `Bearer ${token}`,
				},
			});

			if (!res.ok) throw new Error();
			alert("회원 탈퇴가 완료되었습니다.");
			localStorage.removeItem("token");
			window.location.href = "/";
		} catch {
			alert("회원 탈퇴 실패");
		}
	});

	logoutBtn.addEventListener("click", () => {
		localStorage.removeItem("token");
		alert("로그아웃되었습니다.");
		window.location.href = "/";
	});

	mypageBtn.addEventListener("click", () => {
		window.location.href = "/mypage";
	});

	document.querySelectorAll('.tab-btn').forEach(btn => {
		btn.addEventListener('click', () => {
			const tabName = btn.dataset.tab;

			document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
			document.querySelectorAll('.tab-content').forEach(c => c.style.display = 'none');

			btn.classList.add('active');
			document.getElementById('tab-content-' + tabName).style.display = 'block';

			if (tabName === "written" && writtenData.length > 0) {
				renderPaginatedList(writtenData, "written-list", "written-pagination", "/review");
			}
			if (tabName === "commented" && commentedData.length > 0) {
				renderPaginatedList(commentedData, "commented-list", "commented-pagination", "/comment");
			}
			if (tabName === "liked" && likedData.length > 0) {
				renderPaginatedList(likedData, "liked-list", "liked-pagination", "/review");
			}
		});
	});
	
	
	// 내가 작성한 목록 로직

	function renderPaginatedList(data, listId, paginationId, baseUrl = "") {
		const list = document.getElementById(listId);
		const pagination = document.getElementById(paginationId);
		const itemsPerPage = 5;
		let currentPage = 0;

		function showPage(page) {
			list.innerHTML = "";
			const start = page * itemsPerPage;
			const end = start + itemsPerPage;
			const pageData = data.slice(start, end);

			pageData.forEach(item => {
				const row = document.createElement("tr");

				if (listId === "commented-list") {
					// 댓글용 행 구성 (내용을 링크로 감싸서 상세 페이지로 이동 가능하게 함)
					const contentCell = document.createElement("td");
					const link = document.createElement("a");

					// 게시글 타입에 따라 링크 경로 분기
					if (item.type === "독후감") {
						link.href = `/api/posts/${item.parentId}`;
					} else if (item.type === "QnA") {
						link.href = `/qna_detail?id=${item.parentId}`;
					} else {
						link.href = "#";
					}

					link.textContent = item.content || "";
					contentCell.appendChild(link);
					row.appendChild(contentCell);

					const dateCell = document.createElement("td");
					dateCell.textContent = item.date?.substring(0, 10) || "";
					row.appendChild(dateCell);

				} else {
					// 글/좋아요 탭용 행 구성
					const typeCell = document.createElement("td");
					typeCell.textContent = item.type || "";
					row.appendChild(typeCell);

					const idCell = document.createElement("td");
					idCell.textContent = item.id || "";
					row.appendChild(idCell);

					const titleCell = document.createElement("td");
					const link = document.createElement("a");

					// 게시글 타입에 따라 링크 경로 분기
					if (item.type === "독후감") {
						link.href = `/api/posts/${item.id}`;
					} else if (item.type === "QnA") {
						link.href = `/qna_detail?id=${item.id}`;
					} else {
						link.href = "#";
					}

					link.textContent = item.title || "";
					titleCell.appendChild(link);
					row.appendChild(titleCell);

					const dateCell = document.createElement("td");
					dateCell.textContent = item.date?.substring(0, 10) || "";
					row.appendChild(dateCell);
				}

				list.appendChild(row);
			});

			renderPagination(page);
		}

		function renderPagination(activePage) {
			pagination.innerHTML = "";
			const pageCount = Math.ceil(data.length / itemsPerPage);

			for (let i = 0; i < pageCount; i++) {
				const btn = document.createElement("button");
				btn.textContent = i + 1;
				btn.classList.add("btn", "btn-secondary");
				if (i === activePage) btn.style.fontWeight = "bold";
				btn.addEventListener("click", () => {
					currentPage = i;
					showPage(currentPage);
				});
				pagination.appendChild(btn);
			}
		}

		showPage(currentPage);
	}
});


