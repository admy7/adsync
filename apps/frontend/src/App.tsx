import { Navbar } from "./components/Navbar.tsx";
import { Footer } from "./components/Footer.tsx";

function App() {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <Navbar />
      <main className="flex-grow">
        <div />
      </main>
      <Footer />
    </div>
  );
}export default App;
