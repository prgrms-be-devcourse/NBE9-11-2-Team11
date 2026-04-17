"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function LoginSuccessPage() {
  const router = useRouter();
  const [ready, setReady] = useState(false);

  useEffect(() => {
    setReady(true);
  }, []);

  useEffect(() => {
    if (ready) {
      router.replace("/"); // push 대신 replace 추천
    }
  }, [ready, router]);

  return (
    <div className="flex items-center justify-center h-screen">
      <p>로그인 완료! 이동 중...</p>
    </div>
  );
}